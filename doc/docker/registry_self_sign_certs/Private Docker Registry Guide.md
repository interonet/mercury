# Private Docker Registry Guide

## generate self-sign certificate
```
mkdir -p certs
```

```
openssl req  \
    -newkey rsa:4096 -nodes -sha256 \
    -keyout certs/docker-registry.shuolingdeng.com.key  \
    -x509 -days 365 \
    -out certs/docker-registry.shuolingdeng.com.crt
```

## Run Registry
```
docker run -d -p 5000:5000 --restart=always --name registry \
	-v `pwd`/certs:/certs \
	-e REGISTRY_HTTP_TLS_CERTIFICATE=/certs/domain.crt \
	-e REGISTRY_HTTP_TLS_KEY=/certs/domain.key \
	registry:latest
```

## Change Docker Client Conf
In ```/etc/default/docker```

```
DOCKER_OPTS="--insecure-registry myregistrydomain.com:5000"
```

## Test
```
curl --cacert docker-registry.shuolingdeng.com.crt \
    -X GET https://docker-registry.shuolingdeng.com:5000/v2/_catalog
```

## Ref
1. [Deployment](https://docs.docker.com/registry/deploying/)
2. [Self-Sign](https://docs.docker.com/registry/insecure/#/using-self-signed-certificates)
3. [Test](http://stackoverflow.com/questions/31251356/how-to-get-a-list-of-images-on-docker-registry-v2)