function permgen(a,n)
	n = n or #a
	if n <= 1 then
		printResult(a)
	else
		for i=1,n do
			a[n], a[i] = a[i], a[n]
			permgen(a, n-1)
			a[n], a[i] = a[i], a[n]
		end
	end
end

function printResult(a)
	for i=1,#a do
		io.write(a[i], "")
	end
	io.write("\n")
end


function co_permgen(a,n)
	n = n or #a
	if n <= 1 then
		coroutine.yield(a)
	else
		for i=1,n do
			a[n], a[i] = a[i], a[n]
			permgen(a, n-1)
			a[n], a[i] = a[i], a[n]
		end
	end
end

function permutations(a)
	local co = coroutine.create(function() co_permgen(a) end)
	return function()
		local code, res = coroutine.resume(co)
		return res
	end
end

function test_co()
	for p in permutations({"a","b","c","d","e"}) do
		printResult(p)
	end
end	

--permgen({1,2,3,4})	

--test_co()

for n in pairs(_G) do
	print(n) 
end