IMAGE="jloisel/jpetstore6:latest"

image:
	docker build --pull --rm=true -t $(IMAGE) .

push:
	docker push $(IMAGE)
