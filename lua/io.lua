print('###################')
local BUFSIZE = 2^13
--local f = io.input(arg[1])
local f = io.input("D:\\app\\logs\\message-web\\message-web-all.log")
local cc, lc, wc = 0,0,0
local start = os.clock();
while true do
	local lines, rest = f:read(BUFSIZE, "*line")
	if not lines then break end
	if rest then lines = lines .. rest .. "\n" end
	cc = cc + #lines
	local _, t = string.gsub(lines, "%S+", "")
	wc = wc + t
	_,t = string.gsub(lines, "\n", "\n")
	lc = lc + t
	print(lc,wc,cc)
end
print(lc,wc,cc)	
print(string.format("total time:%.2f\n", os.clock()-start))
io.write("sin(3) = ", math.sin(3))