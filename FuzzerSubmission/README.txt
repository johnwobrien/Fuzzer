README
Fuzzer Part 2
Written 10/15/2014

------------
Dependencies
------------

Execution of this program requires Java to be installed.

Other resources and libraries needed:
  - htmlunit-2.15-OSGi.jar
  - endings.txt


---------
Execution
---------

java -jar Fuzzer.jar [discover | test] URL OPTIONS

COMMANDS:
  discover  Output a comprehensive, human-readable list of all discovered inputs to the system. Techniques include both crawling and guessing. 
  test      Discover all inputs, then attempt a list of exploit vectors on those inputs. Report potential vulnerabilities.

URL:
  Properly formatted URL to run discovery against

OPTIONS:
  --custom-auth=string     Signal that the fuzzer should use hard-coded authentication for a specific application. Optional.
                           (Implemented auths: ‘dvwa’, ‘bodgeit’)

  Discover options:
    --common-words=file    Newline-delimited file of common words to be used in page guessing and input guessing. Required.

  Test options:
    --vectors=file         Newline-delimited file of common exploits to vulnerabilities. Required.
    --sensitive=file       Newline-delimited file data that should never be leaked. It's assumed that this data is in the application's database (e.g. test data), but is not reported in any response. Required.
    --random=[true|false]  When off, try each input to each page systematically.  When on, choose a random page, then a random input field and test all vectors. Default: false.
    --slow=500             Number of milliseconds considered when a response is considered "slow". Default is 500 milliseconds


---------
Examples
---------
  # Discover inputs 
  java -jar Fuzzer.jar discover http://127.0.0.1/ --common-words=default.txt

  # Discover inputs to DVWA using our hard-coded authentication 
  java -jar Fuzzer.jar discover http://127.0.0.1/  --custom-auth=dvwa --common-words=default.txt

  # Discover and Test inputs to DVWA using specified vectors.txt and sensitive.txt files, with random off and slow attack set to 5 seconds.
  java -jar Fuzzer.jar test http://127.0.0.1/  --custom-auth=dvwa --common-words=default.txt --vectors=vectors.txt --sensitive=sensitive.txt --slow=5000
