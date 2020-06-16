IMAGE="jloisel/jpetstore6:latest"

package:
	./mvnw clean package

image: package
	docker build --pull --rm=true -t $(IMAGE) .

push: image
	docker push $(IMAGE)
