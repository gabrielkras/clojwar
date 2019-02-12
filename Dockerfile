# Gets from the Docke Hub the Clojure Image with java8 and lein
FROM clojure:openjdk-8-lein-2.8.3-alpine

# Setting the workdir
WORKDIR /clojwar

# Copping files into the docker image
COPY ./ /clojwar/

# Execute tests
RUN lein test

# Set the docker default command
CMD ["/usr/local/bin/lein", "run"]