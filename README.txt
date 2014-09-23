
README
Fuzzer Part 1
Written 9/22/2014

------------
Dependencies
------------

Execution of this program requires Java to be installed.


---------
Execution
---------

java -jar Fuzzer.jar discover URL OPTIONS

URL:
  Properly formatted URL to run discovery against

OPTIONS: 
  --common-words=file      Newline-delimited file of common words to be used in page guessing and input guessing. Required.
                           (Note: default.txt is packaged with the jar and can be used if desired).

  --custom-auth=string     Signal that the fuzzer should use hard-coded authentication for a specific application. Optional.
                           (Implemented auths: ‘dvwa’, ‘bodgeit’)


---------
Examples
---------
  # Discover inputs 
  java -jar Fuzzer.jar discover http://127.0.0.1/ --common-words=default.txt

  # Discover inputs to DVWA using our hard-coded authentication 
  java -jar Fuzzer.jar discover http://127.0.0.1/  --custom-auth=dvwa --common-words=default.txt