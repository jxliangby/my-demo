package main

import "fmt"

type Books struct{
	title string
	author string
}

func main() {
    var book1 Books
	var book2 Books
	book1.title = "Go"
	book1.author = "K"
	
	book2.title = "java"
	book2.author = "J"
	
	printBook(book1)
	printBook(book2)
	var struct_pointer *Books
	struct_pointer = &book1;
	fmt.Printf( "Book title : %s\n", struct_pointer.title)
}

func printBook( book Books ) {
   fmt.Printf( "Book title : %s\n", book.title)
   fmt.Printf( "Book author : %s\n", book.author)
}