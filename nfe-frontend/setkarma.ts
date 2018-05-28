import { writeFile, writeFileSync, createReadStream, createWriteStream } from 'fs';
//import { argv } from 'yargs';

//const environment = argv.env;
//console.log('enterrr set karmaa '+ environment)
const targetPath='./karma.conf.js';
const srcPath='./karma.demo.conf.js';
createReadStream(srcPath).pipe(createWriteStream(targetPath));
/* copyfiles([srcPath],targetPath,function(err){
  if (err) console.error(err);
}) */
//writeFileSync(targetPath, srcPath);

export class Setkarma {
}
