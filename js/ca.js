const dataFolder = './ins/';
const fs = require('fs');
const { execFile } = require('child_process');

fs.readdir(dataFolder, (err, files) => {
  files.forEach(file => {
    if (file.endsWith(".in")) {
      console.log(file);
      let ls = execFile('node', ['main', dataFolder + file], {shell: true}, 
        (error, stdout, stderr) => {
          if (error) {
          throw error;
          }
          fs.writeFile(dataFolder + file + ".out", stdout, function(err) {
            if(err) {
                return console.log(err);
            }
            console.log("The file " + file + " was saved!");
          });         
        }
      );
    }
  });
});

// fs.readdir(dataFolder, (err, files) => {
//   files.forEach(file => {
//     if (file.endsWith(".out")) {
//       console.log(file);
//       let ls = execFile('node', ['check', dataFolder + file], {shell: true}, 
//         (error, stdout, stderr) => {
//           if (error) {
//           throw error;
//           }
//           fs.appendFile(dataFolder + file + ".score", stdout, function(err) {
//             if(err) {
//                 return console.log(err);
//             }
//             console.log("The file " + file + " was saved!");
//           });         
//         }
//       );
//     }
//   });
// });