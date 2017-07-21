
file = io.open("hello.lua","r")
io.input(file)
for line in io.lines() do
	print(line, "\n")
end

io.close(file)



