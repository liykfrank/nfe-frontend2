import { writeFile, writeFileSync, createReadStream, createWriteStream } from 'fs';
//import { argv } from 'yargs';

//const environment = argv.env;
//console.log('enterrr set proctactor '+ environment)
const targetPath='./protractor.conf.js';
const srcPath='./protractor.demo.conf.js';
createReadStream(srcPath).pipe(createWriteStream(targetPath));
/* copyfiles([srcPath],targetPath,function(err){
  if (err) console.error(err);
}) */
//writeFileSync(targetPath, srcPath);

