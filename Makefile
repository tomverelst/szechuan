targets = common rick morty mr-meeseeks meeseeks-box mcdonalds
docker-targets = rick-docker morty-docker mr-meeseeks-docker meeseeks-box-docker mcdonalds-docker

default: all

.PHONY: all
all: start-adventure $(targets) $(docker-targets) end-adventure

# Installs the node modules for Morty
.PHONY: install-morty
install-morty:
	@$(MAKE) quote
	@(cd morty/frontend && npm install > /dev/null)

# Build applications with mvn
.PHONY: $(targets)
$(targets):
	@if [ "$@" == "morty" ]; then $(MAKE) install-morty; fi;
	@$(MAKE) quote
	@(cd $@ && mvn clean install) > /dev/null

.PHONY: rick-docker
rick-docker:
	@$(MAKE) quote
	@(cd rick && docker build -t tomverelst/rick . > /dev/null)

.PHONY: morty-docker
morty-docker:
	@$(MAKE) quote
	@(cd morty && docker build -t tomverelst/morty . > /dev/null)

.PHONY: mr-meeseeks-docker
mr-meeseeks-docker:
	@$(MAKE) quote
	@(cd mr-meeseeks && docker build -t tomverelst/mr-meeseeks . > /dev/null)

.PHONY: meeseeks-box-docker
meeseeks-box-docker:
	@$(MAKE) quote
	@(cd meeseeks-box && docker build -t tomverelst/meeseeks-box . > /dev/null)

.PHONY: mcdonalds-docker
mcdonalds-docker:
	@$(MAKE) quote
	@(cd mcdonalds && docker build -t tomverelst/mcdonalds . > /dev/null)

# Print out a random quote
.PHONY: quote
quote:
	@(cd quotes && ./quote.sh)

# Start the adventure
.PHONY: start-adventure
start-adventure:
	@echo "A new Rick & Morty adventure!"

.PHONY: end-adventure
end-adventure:
	@echo "The end."

random:
	echo $$(( $(RANDOM) % 4 ))
