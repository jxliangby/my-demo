//版本号
var versionReg = /^(([0-9]+\.[0-9]+){1}(\.[0-9]+)*)$/;
var versionStr1 = "1.0.0";
var versionStr2= "1.0.0.d"; 
var versionStr3= "1.0.5/7"; 
var versionStr4= "1a0b5c7"; 

console.log(versionStr1+"---"+versionReg.test(versionStr1));
console.log(versionStr2+"---"+versionReg.test(versionStr2));
console.log(versionStr3+"---"+versionReg.test(versionStr3));
console.log(versionStr4+"---"+versionReg.test(versionStr4));
jiangyouliang1