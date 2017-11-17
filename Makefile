targets = common rick morty mr-meeseeks meeseeks-box mcdonalds

.PHONY: all
all: start-story $(targets) end-story

# Installs the node modules for Morty
.PHONY: install-morty
install-morty:
	@$(MAKE) quote
	@(cd morty/frontend && npm install > /dev/null)

.PHONY: $(targets)
$(targets):
	@if [ "$@" == "morty" ]; then $(MAKE) install-morty; fi;
	@$(MAKE) quote
	@(cd $@ && mvn clean install) > /dev/null

.PHONY: quote
quote:
	@(cd quotes && ./quote.sh)

.PHONY: start-story
start-story:
	@echo "A new Rick & Morty adventure!"

.PHONY: end-story
end-story:
	@echo "The end."

random:
	echo $$(( $(RANDOM) % 4 ))
