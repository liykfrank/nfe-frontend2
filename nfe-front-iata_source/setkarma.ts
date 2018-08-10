import { writeFile, writeFileSync, createReadStream, createWriteStream } from 'fs';

const targetPath = './karma.conf.js';
const srcPath = './karma.demo.conf.js';

createReadStream(srcPath).pipe(createWriteStream(targetPath));

export class Setkarma {
}
