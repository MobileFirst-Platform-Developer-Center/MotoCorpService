# Running sample NodeJS

```bash
eval $(docker-machine env dev-dns-host01)

docker build -t centos-node-hello ./demo_server/

#to remove a older version if it is available
docker stop onprem_crm && docker rm onprem_crm

docker run  --name onprem_crm  -p 49160:8080 -d centos-node-hello

# redeploy production:
docker stop onprem_crm && docker rm onprem_crm && docker run  --name onprem_crm  -p 49160:8080 -d centos-node-hello

# test production
#list cusotmers
curl -X GET http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/

curl -X PUT -H "Content-Type: application/json" -d '{ "name": "New Name - B" }' http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/1 -vv

```

### Tests
```bash


#test interative mode
docker stop onprem_crm_test && docker rm onprem_crm_test
docker run  --name onprem_crm_test  -p 49161:8080 -it centos-node-hello /bin/bash



docker build --no-cache -t centos-node-hello ./demo_server/ && docker stop onprem_crm_test && docker rm onprem_crm_test && docker run  --name onprem_crm_test  -p 49161:8080 -d centos-node-hello

docker build -t centos-node-hello ./demo_server/ && docker stop onprem_crm_test && docker rm onprem_crm_test && docker run  --name onprem_crm_test  -p 49161:8080 -d centos-node-hello

# run local folder:
docker run  --name onprem_crm_test -v .:/src -p 49161:8080 -d centos-node-hello

#to test
curl -i $DOCKER_HOST_IP:49160
#e.g.:
curl -i 192.168.99.100:49160


# Debuging
docker run  --name onprem_crm  -p 49160:8080 -it centos-node-hello /bin/bash


# Testing NodeApp:
# Secury Gateway endpoint:
#list cusotmers
curl -X GET http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/
#create customer
curl -X POST http://cap-sg-prd-2.integration.ibmcloud.com:15330/customers/ --data-binary "@/Users/cesarlb/_DEV_/_2016/CloudPatternApps/demo3/work_master/MotoCorpService/onPremSimulator/Customer.PUT.json"  --header "Content-Type:application/json"



# update customer
curl -X PUT


data = {a:1, b:2, c: {d:1}};
data_delta = {a:3, c: {d:3,e:2}};

Testing:
curl -X PUT -H "Content-Type: application/json" -d '{ "name": "New Name - B" }' "http://192.168.99.100:49161/customers/1" -vv
curl -X GET http://192.168.99.100:49161/customers/ -vv
curl -X GET http://192.168.99.100:49161/customers/1 -vv

curl -X PUT -H "Content-Type: application/json" -d '{ "name": "New Name - A" }' "http://192.168.99.100:49161/customers/1" -vv
curl -X GET http://192.168.99.100:49161/customers/1 -vv

```
