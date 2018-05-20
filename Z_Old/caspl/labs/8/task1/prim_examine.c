
#define _FILE_OFFSET_BITS 64
#define EI_DATA 5

#include <stdio.h>
#include <stdlib.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include "elf.h"

int check_magic_numbers(Elf32_Ehdr *header, int print)
{
  if (print)
  {
    printf("magic numbers are: %x %x %x %x\n", header->e_ident[0], header->e_ident[1], header->e_ident[2], header->e_ident[3]);
  }
  
  if (  header->e_ident[0] == 0x7f &&
    header->e_ident[1] == 'E' &&
    header->e_ident[2] == 'L' &&
    header->e_ident[3] == 'F')
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

 
/**
 * code for mapping is based on http://www.cs.bgu.ac.il/~caspl152/Lab7/Mmap
 */
int main(int argc, char **argv) {
  int fd;
  void *map_start; /* will point to the start of the memory mapped file */
  struct stat fd_stat; /* this is needed to the size of the file */
  Elf32_Ehdr *header; /* this will point to the header structure */
  int num_of_section_headers;
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
  
  print_data_encoding(header);
  print_entry_point(header);
  print_section_header_offset(header);
  print_num_of_section_headers(header);
  print_size_of_section_header(header);
  print_program_table_offset(header);
  print_program_header_entry(header);
  print_size_of_program_header_entry(header);
  
  num_of_section_headers = header->e_shnum;
  
  
  

  /* now, we unmap the file */
  munmap(map_start, fd_stat.st_size);

  
  return 0;
}
