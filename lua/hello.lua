function fact(n)
	if n==0 then
		return 1
	else
		return n * fact(n-1)
	end
end

mytable = {color="blue",thickness=2,haha=true }
		  
polyine = {color="blue",  {x=-990,y=2},thickness=2,haha=true,
		  {x=0,y=2},{x=12,y=99},{x=134,y=9876},{x=111,y=222},{x=2455,y=789}
		  }

print("enter a number:")
a = io.read("*number")
print(fact(a))
print(polyine["color"]..polyine[1].x..polyine[5].x)	

for i,j in pairs(mytable) do
	print(i,j)
end
for i,j in ipairs(polyine) do
	print(i,j.x)
end
for i,j in pairs(polyine) do
	if type(j)=="table" then
		print(i,j.x)
	else	
		print(i,j)
	end	
end
	