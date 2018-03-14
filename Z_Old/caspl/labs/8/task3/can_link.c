
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <sys/mman.h>
#include <errno.h>
#include <fcntl.h>
#include "elf.h"

typedef struct {
    void *maddr;            /* Start of mapped Elf binary segment in memory */
    int mlen;               /* Length in bytes of the mapped Elf segment */
    Elf32_Ehdr *ehdr;       /* Start of main Elf header (same as maddr) */
    Elf32_Sym *symtab;      /* Start of symbol table */
    Elf32_Sym *symtab_end;  /* End of symbol table (symtab + size) */
    char *strtab;           /* Address of string table */
    Elf32_Sym *dsymtab;     /* Start of dynamic symbol table */
    Elf32_Sym *dsymtab_end; /* End of dynamic symbol table (dsymtab + size) */
    char *dstrtab;          /* Address of dynamic string table */ 
} Elf_obj;

typedef struct {
    char *sym_name;           /* Address of string table */
		struct Sym_node *next;
		int is_defined;
} Sym_node;

char * start_check(Elf_obj *eo);


Elf_obj *elf_open(char *filename);
void elf_close(Elf_obj *ep); 
Elf32_Sym *elf_firstsym(Elf_obj *ep);
Elf32_Sym *elf_nextsym(Elf_obj *ep, Elf32_Sym *sym);
char *elf_symname(Elf_obj *ep, Elf32_Sym *sym);
int elf_is_defined(Elf32_Sym *sym);

void get_symbol_list(Elf_obj *eo);
int add_sym_node(char * symbol_name, int defined);
void delete_sym_list();
void make_sure_all_defined();

char *elf_dsymname(Elf_obj *ep, Elf32_Sym *sym);
Elf32_Sym *elf_firstdsym(Elf_obj *ep);
Elf32_Sym *elf_nextdsym(Elf_obj *ep, Elf32_Sym *sym);

Sym_node *sym_head = NULL;

int main(int argc, char **argv) {
	if (argc < 3) {
		printf("please specify 2 files to load.\n");
		exit(0);
	}


	Elf_obj *eo = elf_open(argv[1]);

	/* Iterate over the symbol table, file 1 */
	get_symbol_list(eo);
	elf_close(eo);
	eo = elf_open(argv[2]);
	get_symbol_list(eo);
	printf("duplicate check: PASSED\n");
	char * found = start_check(eo);

	make_sure_all_defined();
	if (strcmp("FAILED", found) == 0) {
		/* file 2 */	
		found = start_check(eo);
	}

	printf("\n_start check: %s\n", found);
	if (strcmp("FAILED", found) == 0) {
		exit(-1);
	}

	elf_close(eo);
	delete_sym_list();
  return 0;
}

char * start_check(Elf_obj *eo) {
	Elf32_Sym *sym;
	for (sym = elf_firstsym(eo); (int) sym < (int) eo->symtab_end; sym = elf_nextsym(eo, sym)) {
		 if (strcmp("_start", elf_symname(eo, sym)) == 0) {
		 	 return "PASSED";
		 }
	}

	return "FAILED";		
}

/*
 * elf_open - Map a binary into the address space and extract the
 * locations of the static and dynamic symbol tables and their string
 * tables. Return this information in a Elf object file handle that will
 * be passed to all of the other elf functions.
 */
Elf_obj *elf_open(char *filename) 
{
    int i, fd;
    struct stat sbuf;
    Elf_obj *ep;
    Elf32_Shdr *shdr;

    ep = (Elf_obj *) malloc(sizeof(Elf_obj));

    /* Do some consistency checks on the binary */
    fd = open(filename, O_RDONLY);
    if (fd == -1) {
			fprintf(stderr,	"Can't open \"%s\": %s\n", filename, strerror(errno));
			exit(1);
    }
    if (fstat(fd, &sbuf) == -1) {
			fprintf(stderr, "Can't stat \"%s\": %s\n", filename, strerror(errno));
			exit(1);
    }
    if (sbuf.st_size < sizeof(Elf32_Ehdr)) {
			fprintf(stderr, "\"%s\" is not an ELF binary object\n",	filename);
			exit(-2);
    }

    /* It looks OK, so map the Elf binary into our address space */
    ep->mlen = sbuf.st_size;
    ep->maddr = mmap(NULL, ep->mlen, PROT_READ, MAP_SHARED, fd, 0);
    if (ep->maddr == (void *)-1) {	
			fprintf(stderr,	"Can't mmap \"%s\": %s\n", filename, strerror(errno));
			exit(1);
    }
    close(fd);

    /* The Elf binary begins with the Elf header */
    ep->ehdr = ep->maddr;

    /* Make sure that this is an Elf binary */ 
    if (strncmp(ep->ehdr->e_ident, ELFMAG, SELFMAG)) {
			fprintf(stderr, "\"%s\" is not an ELF binary object\n",	filename);
			exit(-2);
    }

    shdr = (Elf32_Shdr *)(ep->maddr + ep->ehdr->e_shoff);
    for (i = 0; i < ep->ehdr->e_shnum; i++) {
			if (shdr[i].sh_type == SHT_SYMTAB) {   /* Static symbol table */
		    ep->symtab = (Elf32_Sym *)(ep->maddr + shdr[i].sh_offset);
		    ep->symtab_end = (Elf32_Sym *)((char *)ep->symtab + shdr[i].sh_size);
		    ep->strtab = (char *)(ep->maddr + shdr[shdr[i].sh_link].sh_offset);
			}
			if (shdr[i].sh_type == SHT_DYNSYM) {   /* Dynamic symbol table */
		    ep->dsymtab = (Elf32_Sym *)(ep->maddr + shdr[i].sh_offset);
		    ep->dsymtab_end = (Elf32_Sym *)((char *)ep->dsymtab + shdr[i].sh_size);
		    ep->dstrtab = (char *)(ep->maddr + shdr[shdr[i].sh_link].sh_offset);
			}
    }
    return ep;
}

/*
 * elf_nextsym - Return ptr to next symbol in static symbol table,
 * or NULL if no more symbols.
 */
Elf32_Sym *elf_nextsym(Elf_obj *ep, Elf32_Sym *sym)
{
  sym++;
  if (sym < ep->symtab_end)
		return sym;
  else
		return NULL;
}	
/*
 * elf_firstsym - Return ptr to first symbol in static symbol table
 */
Elf32_Sym *elf_firstsym(Elf_obj *ep)
{
    return ep->symtab;
}

int elf_is_defined( Elf32_Sym *sym)
{
	if (sym->st_shndx == SHN_UNDEF) {
    return 0;
	}

  return 1;
}


/*
 * elf_symname - Return ASCII name of a static symbol
 */
char *elf_symname(Elf_obj *ep, Elf32_Sym *sym) {
    return &ep->strtab[sym->st_name];	
}

/* 
 * elf_close - Free up the resources of an  elf object
 */
void elf_close(Elf_obj *ep) 
{
    if (munmap(ep->maddr, ep->mlen) < 0) {
	perror("munmap");
	exit(1);
    }
    free(ep);
}


void get_symbol_list(Elf_obj *eo) {
	Elf32_Sym *sym;
	int is_defined = 0;
	for (sym = elf_firstsym(eo); (int) sym < (int) eo->symtab_end; sym = elf_nextsym(eo, sym)) {
		is_defined = elf_is_defined(sym); 
		char * name = elf_symname(eo, sym);

		if (add_sym_node(name, is_defined) == -1) {
			printf("Failed adding symbol\n");
			exit(-1);
		}
	}
	for (sym = elf_firstdsym(eo); (int) sym < (int) eo->symtab_end; sym = elf_nextdsym(eo, sym)) {
		is_defined = elf_is_defined(sym); 
		char * name = elf_symname(eo, sym);

		if (add_sym_node(name, is_defined) == -1) {
			printf("Failed adding symbol\n");
			exit(-1);
		}
	}
}

int add_sym_node(char * symbol_name, int defined) {
	if (strcmp(symbol_name, "") == 0 ) {
		return 0;
	}
	Sym_node *node_it = sym_head;

	if (node_it == NULL) {
		/* head is null */
		sym_head = (Sym_node*) malloc(sizeof(Sym_node));
		sym_head->sym_name = malloc(sizeof(symbol_name));
		strcpy(sym_head->sym_name, symbol_name);
		sym_head->is_defined = defined; 
		sym_head->next = NULL;
		return 0;
	}	
	Sym_node *prev_node = sym_head;
	while (node_it != NULL) {
		if (strcmp(symbol_name, node_it->sym_name) == 0) {
			/* Found duplicate */
			if (defined == 1 && node_it->is_defined > 0) {
				printf("duplicate check: FAILED (%s)\n", symbol_name);
				return -1;
			}
			else {
				node_it->is_defined = defined;
			}
		}

		prev_node = (Sym_node *) node_it;
		node_it =(Sym_node *)  node_it->next;
	}

	/* create new node */
	node_it = (Sym_node*) malloc(sizeof(Sym_node));
	node_it->sym_name = malloc(sizeof(symbol_name));
	strcpy(node_it->sym_name, symbol_name);
	node_it->is_defined = defined;	
	node_it->next = NULL;
	prev_node->next = (struct Sym_node *) node_it;
	return 0;
}

void delete_sym_list() {
	Sym_node *node_it = sym_head;
	Sym_node *prev_node = sym_head;

	while(node_it != NULL) {
		free(node_it->sym_name);
		prev_node = (Sym_node *)  node_it;
		node_it = (Sym_node *) node_it->next;
		free(prev_node);
	}
}

void make_sure_all_defined(){
	Sym_node *node_it = sym_head;

	while(node_it != NULL) {
		if (node_it->is_defined == 0) {
			printf("no missing symbols: FAILED (%s)\n", node_it->sym_name);
			exit(-1);
		}
		node_it = (Sym_node *) node_it->next;
	}
	printf("no missing symbols: PASSED");
}

/*
 * elf_dsymname - Return ASCII name of a dynamic symbol
 */ 
char *elf_dsymname(Elf_obj *ep, Elf32_Sym *sym)
{
    return &ep->dstrtab[sym->st_name];
}


/*
 * elf_firstdsym - Return ptr to first symbol in dynamic symbol table
 */
Elf32_Sym *elf_firstdsym(Elf_obj *ep)
{
    return ep->dsymtab;
}

/*
 * elf_nextdsym - Return ptr to next symbol in dynamic symbol table,
 * of NULL if no more symbols.
 */ 
Elf32_Sym *elf_nextdsym(Elf_obj *ep, Elf32_Sym *sym)
{
    sym++;
    if (sym < ep->dsymtab_end)
	return sym;
    else
	return NULL;
}
