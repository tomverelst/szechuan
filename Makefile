targets = common rick morty mr-meeseeks meeseeks-box mcdonalds

.PHONY: all
all: $(targets)

.PHONY: install-morty
install-morty:
	(cd morty/frontend && npm install)

.PHONY: $(targets)
$(targets):
	if [ "$@" == "morty" ]; then make install-morty; fi;
	(cd $@ && mvn clean install)
