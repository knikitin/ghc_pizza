const fs = require('fs');

const input = fs.readFileSync(process.argv[2], 'utf8').trim().split(/\n/);
const [R, C, L, H] = input[0].split(/\s/).map(Number);
const lines = input.slice(1);
slices = [];

lines.forEach((l,y) => {
  [...l].forEach((ch, x) => {
    if (ch != 'X') {
      for (let i=0; i < H; i++) {
        if ((i+x)>= C) {
          break;
        }
        for (let j=0; j < H; j++) {
          if ((j+y)>= R) {
            break;
          }
          if ((i + 1)*(j + 1) > H ) {
            break;
          }
          cp = '';
          for (let cr = 0; cr <= j; cr++){
            cp += lines[y+cr].substring(x, x+i+1);
          }
          cT = (cp.match(/T/g)||[]).length;
          cM = (cp.match(/M/g)||[]).length;
          cX = (cp.match(/X/g)||[]).length;
          if ((cX == 0) && (cT >= L) && (cM >= L)) {
            fy = y + j;
            fx = x + i;
            slices.push({y,x,fy,fx});
            for (let xj=0; xj <= j; xj++){
              cy = y+xj;
              s1 = x > 0 ? lines[cy].substring(0,x):'';
              s2 = 'X'.repeat(i+1);
              s3 = x+i+1 <= C ? lines[cy].substring(x+i+1):'';
              lines[cy] = s1+s2+s3;
            }             
          }  
        }
      }
    }
  })
})

console.log(slices.length+'');
slices.forEach(e => {
  console.log(e.y + ' ' + e.x + ' ' + e.fy + ' ' + e.fx)
});
