Java Library for blink(1)
-------------------------

### Build

```
./mvnw clean install
```

This builds a fat jar in: `blink1-library/target/blink1-library-jar-with-dependencies.jar`

### Examples

Examples that use the library can be found in the blink1-examples module

you can run an example from the command line (after building) with:

```
./run-example.sh Example<n>
```

(where `<n>` is: 0, 1, 2 or 3)

ex:

```
./run-example.sh Example0
Looking for blink(1) devices...
blink(1)s found:
i:0  serial:37ec9b94
```

### Toy / Test Applications written in Processing Java ###

These "toy" or "test" applications for just playing around with your blink(1):
- BlinkATweet -- Watch real-time Twitter stream for keywords, flash blink(1) when found.  
Download:
[(Mac OSX)](https://thingm.com/blink1/downloads/BlinkATweet-mac.zip) /
[(Windows)](https://thingm.com/blink1/downloads/BlinkATweet-win.zip)

- Blink1ColorOrgan -- Sound-reactive program, music-to-color, instant disco!
Download:
[(Mac OSX)](https://thingm.com/blink1/downloads/Blink1ColorOrgan-mac.zip) /
[(Windows)](https://thingm.com/blink1/downloads/Blink1ColorOrgan-win.zip)

- Blink1ColorPicker -- Play with blink(1) select colors.
Download:
[(Mac OSX)](https://thingm.com/blink1/downloads/Blink1ColorPicker-mac.zip),
[(Windows)](https://thingm.com/blink1/downloads/Blink1ColorPicker-win.zip)

- Blink1Test0 -- a very simple random-color app.
Download:
[(Mac OSX)](https://thingm.com/blink1/downloads/Blink1Test0-mac.zip) /
[(Windows)](https://thingm.com/blink1/downloads/Blink1Test0-win.zip)
