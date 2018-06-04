FROM node:alpine

RUN npm install -g http-server
RUN npm install -g @angular/cli

COPY ./dist/ /nfe/

CMD http-server /nfe