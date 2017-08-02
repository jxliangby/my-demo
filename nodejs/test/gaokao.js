
var fs = require('fs'), xlsx = require('xlsx');

function parse(fileName) {  
    var book = xlsx.readFileSync(fileName), result = [];
    book.SheetNames.forEach(function(name){
        var sheet = book.Sheets[name],
            range = xlsx.utils.decode_range(sheet['!ref']),
            row_start = 3, row_end = range.e.r,
            col_start = range.s.c, col_end = range.e.c,
            row_data, i, addr, cell;
		console.log(range);	
        for(;row_start<=row_end;row_start++) {
            row_data = [];
            for(i=col_start;i<=col_end;i++) {
                addr = xlsx.utils.encode_col(i) + xlsx.utils.encode_row(row_start);
                cell = sheet[addr];
                if(cell.l) {
                    row_data.push({text: cell.v, link: cell.l.Target});
                } else 
				{
                    row_data.push(cell.v);
                }
            }
            result.push(row_data);
        }
       // result[name] = rows;
    });
    return result;
}
console.log('parse all colleges');
//所有学校以及对应的专业
var colleges = parse("普通类平行计划3_普通类平行录取_物理化学生物.xlsx");

console.log('parse all jsj');
//所有计算机相关的学校专业
var jsjs = colleges.filter((s)=>{
	return s[4].indexOf('计算机')!=-1;
});
console.log('parse all benke');
//所有本科学校
var benkenjsj = jsjs.filter((s)=>{
	return s[8]=='本科';
});

fs.writeFileSync('gaokao-jsjs.txt', jsjs.join(',\r\n'));
fs.writeFileSync('gaokao-benkenjsj.txt', benkenjsj.join(',\r\n'));