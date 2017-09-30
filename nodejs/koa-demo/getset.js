var _age = 18;

var person = {
	get age(){
		return _age;
	},
	
	set age(val){
		if(val >100) _age = new Date().getFullYear()-val;
		else _age = val;
	}
}
console.log('1-----age:'+person.age);
person.age = 30;
console.log('2-----age:'+person.age);
person.age = 136;
console.log('3-----age:'+person.age);
