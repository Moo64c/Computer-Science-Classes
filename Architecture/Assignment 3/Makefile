all:
	gcc -g -Wall -fno-omit-frame-pointer base.c -o base

bin/asm_ass1.o:
	mkdir -p bin
	nasm -f elf64 asm_ass1.s -o bin/asm_ass1.o -l bin/asm_ass1.lst

test: clean all
	./base

.PHONY: clean
clean:
	rm -rf bin base
