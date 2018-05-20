all: bin/asm_ass1.o
	gcc -g -Wall -fno-omit-frame-pointer main_ass1.c bin/asm_ass1.o -o calc

bin/asm_ass1.o:
	mkdir -p bin
	nasm -f elf64 asm_ass1.s -o bin/asm_ass1.o -l bin/asm_ass1.lst

test: clean all
	./calc < foo.txt

.PHONY: clean
clean:
	rm -rf bin calc
