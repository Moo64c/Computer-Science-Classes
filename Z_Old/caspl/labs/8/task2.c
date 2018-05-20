
#define _FILE_OFFSET_BITS 64
#define SYMBOL_TABLE_NAME ".symtab"
#define SYMBOL_STRING_TABLE_NAME ".strtab"

#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include "elf.h"


void read_section_header_entities(void *map_start, int num_of_headers, int offset, int section_size, int sh_str_table_index);

void read_header_entity_rec(Elf32_Shdr *s_header, int num_of_headers, int index, char *str_table);

void read_single_header_entity(Elf32_Shdr *s_header, int index, char *str_table);




int check_magic_numbers(Elf32_Ehdr *header, int print)
{
  if (print)
  {
    printf("magic numbers are: %x %x %x %x\n", header->e_ident[0], header->e_ident[1], header->e_ident[2], header->e_ident[3]);
  }
  
  if (  header->e_ident[EI_MAG0] == ELFMAG0 &&
    header->e_ident[EI_MAG1] == ELFMAG1 &&
    header->e_ident[EI_MAG2] == ELFMAG2 &&
    header->e_ident[EI_MAG3] == ELFMAG3)
  {
    return 1;
  }
  
  return 0;

}

void print_data_encoding(Elf32_Ehdr *header)
{
  if (header->e_ident[EI_DATA] == 1)
    printf("data encoding: ELFDATA2LSB\n");
  else
    printf("data encoding: ELFDATA2MSB\n");
}

void print_entry_point(Elf32_Ehdr *header)
{
  printf("entry point: %x\n", header->e_entry);
}

void print_section_header_offset(Elf32_Ehdr *header)
{
  printf("section header offset: %d\n", header->e_shoff);
}

void print_num_of_section_headers(Elf32_Ehdr *header)
{
  printf("number of section header entities: %d\n", header->e_shnum);
}

void print_size_of_section_header(Elf32_Ehdr *header)
{
  printf("section header entity size: %d\n", header->e_shentsize);
}

void print_program_table_offset(Elf32_Ehdr *header)
{
  printf("program header offset: %d\n", header->e_phoff);
}

void print_program_header_entry(Elf32_Ehdr *header)
{
  printf("number of program header entities: %d\n", header->e_phnum);
}

void print_size_of_program_header_entry(Elf32_Ehdr *header)
{
  printf("program header entity size: %d\n", header->e_phentsize);
}

char *get_string_table(void *map_start, Elf32_Shdr *s_header, int sh_str_table_index)
{
  Elf32_Shdr *string_table_header = s_header + sh_str_table_index;
  return (char *) map_start + string_table_header->sh_offset;
}

void read_section_header_entities(void *map_start, int num_of_headers, int offset, int section_size,  int sh_str_table_index)
{
  char *str_table;
  
  Elf32_Shdr *s_header = (Elf32_Shdr *) ( map_start + offset);
  str_table = get_string_table(map_start, s_header, sh_str_table_index);
  read_header_entity_rec(s_header, num_of_headers, 0, str_table);
}

void read_header_entity_rec(Elf32_Shdr *s_header, int num_of_headers, int index, char *str_table)
{
  if (num_of_headers == 0)
    return;
  else
  {
    read_single_header_entity(s_header, index, str_table);
    read_header_entity_rec((Elf32_Shdr *) ( ((char *) s_header) +  sizeof(Elf32_Shdr)), num_of_headers - 1, index + 1, str_table);
  }
}


void read_single_header_entity(Elf32_Shdr *s_header, int index, char *str_table)
{
  printf("[%d]\t%-30s\t%x\t%d\t%d\n", index, str_table + s_header->sh_name, s_header->sh_addr, s_header->sh_offset, s_header->sh_size);
}



Elf32_Shdr *get_section_table_pointer(void *map_start, Elf32_Shdr *s_header, char *table_name, char *str_table, int num_of_headers)
{
  int i;
  for (i=0; i < num_of_headers; i++)
  {
    if (strcmp(str_table + s_header->sh_name, table_name) == 0)
    {
      return s_header;
    }
    s_header = (Elf32_Shdr *) ( ((char *) s_header) +  sizeof(Elf32_Shdr));
  }
  
  return 0;
}

Elf32_Sym *get_symbol_table(void *map_start, Elf32_Shdr *s_header, char *str_table, int num_of_headers)
{ 
  s_header = get_section_table_pointer(map_start, s_header, SYMBOL_TABLE_NAME, str_table, num_of_headers);
  return (Elf32_Sym *) (map_start + s_header->sh_offset);
}


char *get_symbol_string_table(void *map_start, Elf32_Shdr *s_header, char *str_table, int num_of_headers)
{
  s_header = get_section_table_pointer(map_start, s_header, SYMBOL_STRING_TABLE_NAME, str_table, num_of_headers);
  return (char *) (map_start + s_header->sh_offset);
}


void read_single_symtable_entity(Elf32_Sym *sym_ent, int index, char *str_table, char *header_str_table, Elf32_Shdr *s_header)
{ 
  switch (sym_ent->st_shndx)
  {
    case(SHN_ABS):
      printf("[%d]\t%x\t%d\t%-30s\t%-30s\n", index, sym_ent->st_value, sym_ent->st_shndx, "ABS", str_table + sym_ent->st_name);
      break;
    case(SHN_COMMON):
      printf("[%d]\t%x\t%d\t%-30s\t%-30s\n", index, sym_ent->st_value, sym_ent->st_shndx, "COMMON", str_table + sym_ent->st_name);      
      break;
    case(SHN_UNDEF):
      printf("[%d]\t%x\t%d\t%-30s\t%-30s\n", index, sym_ent->st_value, sym_ent->st_shndx, "UNDEFINED", str_table + sym_ent->st_name);            
      break;
    default:
      printf("[%d]\t%x\t%d\t%-30s\t%-30s\n", index, sym_ent->st_value, sym_ent->st_shndx, header_str_table + (s_header + sym_ent->st_shndx)->sh_name, str_table + sym_ent->st_name);
      break;
  }
}


void read_sybol_table_entities_rec(Elf32_Sym *sym_table, char *sym_str_table, char *header_str_table, int num_of_symtable_entities, int index, Elf32_Shdr *s_header)
{
  if (num_of_symtable_entities == 0)
    return;
  
  read_single_symtable_entity(sym_table, index, sym_str_table, header_str_table, s_header);
  read_sybol_table_entities_rec(sym_table + 1, sym_str_table, header_str_table, num_of_symtable_entities - 1, index + 1, s_header);
}



void read_symbol_table_entities(void *map_start, int num_of_headers, int offset, int section_size,  int sh_str_table_index)
{
  char *str_table, *sym_str_table;
  Elf32_Sym *sym_table;
  int num_of_symtable_entities;
  Elf32_Shdr *symtab_header;
  
  
  Elf32_Shdr *s_header = (Elf32_Shdr *) ( map_start + offset);
  str_table = get_string_table(map_start, s_header, sh_str_table_index);
  sym_table = get_symbol_table(map_start, s_header, str_table, num_of_headers);
  sym_str_table = get_symbol_string_table(map_start, s_header, str_table, num_of_headers);
  
  
  symtab_header = get_section_table_pointer(map_start, s_header, SYMBOL_TABLE_NAME, str_table, num_of_headers);
  
  
  num_of_symtable_entities = symtab_header->sh_size / symtab_header->sh_entsize;
  
  
  printf("%d, %d\n", symtab_header->sh_size , symtab_header->sh_entsize);
  
  read_sybol_table_entities_rec(sym_table, sym_str_table, str_table, num_of_symtable_entities, 0, s_header);
}



 
/**
 * code for mapping is based on http://www.cs.bgu.ac.il/~caspl152/Lab7/Mmap
 */
int main(int argc, char **argv) {
  int fd;
  void *map_start; /* will point to the start of the memory mapped file */
  struct stat fd_stat; /* this is needed to the size of the file */
  Elf32_Ehdr *header; /* this will point to the header structure */
  if (argc < 2)
  {
    printf("no file to examine\n");
    exit(1);
  }
  
  if( (fd = open(argv[1], O_RDWR)) < 0 ) {
     perror("error in open");
     exit(-1);
  }

  if( fstat(fd, &fd_stat) != 0 ) {
     perror("stat failed");
     exit(-1);
  }

  if ( (map_start = mmap(0, fd_stat.st_size, PROT_READ | PROT_WRITE , MAP_SHARED, fd, 0)) <0 ) {
     perror("mmap failed");
     exit(-4);
  }

  /* now, the file is mapped starting at map_start.
   * all we need to do is tell *header to point at the same address:
   */

  header = (Elf32_Ehdr *) map_start;
  /* now we can do whatever we want with header!!!!
   * for example, the number of section header can be obtained like this:
   */
  
  if (!check_magic_numbers(header, 1))
  {
    printf("this isn't a valid ELF file, and U R Not Chuck Norris. Aborting!\n");
    exit(-1);
  }
   
  read_symbol_table_entities(map_start, header->e_shnum, header->e_shoff, header->e_shentsize, header->e_shstrndx);
  
  
  

  /* now, we unmap the file */
  munmap(map_start, fd_stat.st_size);

  
  return 0;
}
