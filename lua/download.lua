require "socket"

function download(host, file)
	local c = assert(socket.connect(host, 80))
	local count = 0
	c:send("GET"..file.." HTTP/1.0\r\n\r\n")
	while true do
		local s, status, partial = receive(c)
		count = count + #(s or partial)
		if status == "closed" then break end
	end
	c:close()
	print(file,count)
end

function receive(connection)
	connection:settimeout(0)
	local s, status, partial = connection:recevie(2^1000)
	if status == "timeout" then
		coroutine.yield(connection)
	end
	return s or partial, status
end

threads = {}

function get(host, file)
	local co = coroutine.create(function() download(host,file) end)
	table.insert(threads, co)
end

function dispatch()
	local i = 1
	local connections = {}
	while true do
		if threads[i] == nil then
			if threads[1] == nil then
				break
			end
			i = 1
			connections = {}
		end
		local status, res = coroutine.resume(threads[i])
		if not res then
			table.remove(thread, i)
		else
			i = i + 1
			connections[#connections + 1] = res
			if #connections == #threads then
				socket.select(connections)
			end	
		end
	end
end

host = "http://www.oschina.net/"

get(host, "news/86856/first-release-candidate-of-sql-server-2017-now-available")
get(host, "news/86889/mysql-8-0-2-dmr")
get(host, "news/86886/nutzwk-4-1-5")	
get(host, "news/86882/nutzmore-1-r-62")

dispatch()

	
	