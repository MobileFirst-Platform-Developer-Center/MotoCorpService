# Running sample NodeJS

```bash
eval $(docker-machine env dev-dns-host01)

docker build -t centos-node-hello ./demo_server/

#to remove a older version if it is available
docker stop onprem_crm && docker rm onprem_crm

docker run  --name onprem_crm  -p 49160:8080 -d centos-node-hello

#to test
curl -i $DOCKER_HOST_IP:49160
#e.g.:
curl -i 192.168.99.100:49160


# Debuging
docker run  --name onprem_crm  -p 49160:8080 -it centos-node-hello /bin/bash
```
