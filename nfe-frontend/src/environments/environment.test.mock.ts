import {environment as devMock} from './environment.mock';

export const environment =  Object.assign({}, devMock, {
  basePath: 'http://localhost:9876/'
});
