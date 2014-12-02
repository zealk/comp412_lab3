# If the first argument is "run"...
ifeq (run,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

ifeq (viz,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

ifeq (sim,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

ifeq (trace,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

ifeq (profile,$(firstword $(MAKECMDGOALS)))
  # use the rest as arguments for "run"
  RUN_ARGS := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
  # ...and turn them into do-nothing targets
  $(eval $(RUN_ARGS):;@:)
endif

all:
	@sh test_all.sh 
run:
	@./schedule $(RUN_ARGS)
INPUT = $(shell cat $(RUN_ARGS) | egrep INPUT | awk '{for(i = 3; i <= NF; i++){printf "%s ",$$i}}')
OUTPUT = $(shell cat $(RUN_ARGS) | egrep OUTPUT | awk '{for(i = 2; i <= NF; i++){printf "%s ",$$i}}')

trace:
	@./schedule $(RUN_ARGS) > a.i
	lab3sim/sim < a.i -t -s 1
	@rm -f a.i

sim:
	@./schedule $(RUN_ARGS) > a.i
	@echo "The origin result" 
	@echo $(OUTPUT)
	@lab3sim/sim < $(RUN_ARGS) $(INPUT) | tr '\n' ' '
	@echo "\nMy result"
	@lab3sim/sim < a.i $(INPUT) -s 0 | tr '\n' ' '
	@echo "\n"
	@rm -f a.i
profile:
	@echo "Profiling timing/T$(RUN_ARGS)k.i"
	@python -m cProfile -s 'tottime' source/iloc_instruction_scheduler.py timing/T$(RUN_ARGS)k.i