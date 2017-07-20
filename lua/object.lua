
Account = {
	balance = 0
}

function Account:withdraw(v)
	self.balance = self.balance -v
end

function Account:deposit(v)
	self.balance = self.balance + v
end

function Account:new(o)
	o = o or {}
	setmetatable(o, self)
	self.__index = self
	return o
end

Account.deposit(Account,200)
Account:deposit(200)
print(Account.balance)

a = Account:new({balance=0})
a:deposit(110)
a:withdraw(100)
print(a.balance)

b = Account:new()
b:deposit(190)
b:withdraw(70)
print(b.balance)