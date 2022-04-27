IMAGE="jloisel/jpetstore6:latest"

package:
	./mvnw clean package -DskipTests -DskipUpdateLicense
	mv target/jpetstore.war target/ROOT.war

image: package
	docker build --pull --rm=true -t $(IMAGE) .

run: image
	docker run -p 8080:8080 --rm $(IMAGE)

push: image
	docker push $(IMAGE)
