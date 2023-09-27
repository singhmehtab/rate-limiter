# Rate-limiter

There are 4 implementations here.
1. Token Bucket 
2. Fixed Window
3. Sliding Window Counter
4. Sliding Window counter with redis for distributed applications.


This application is config based with all the config in application.properties file and Constants class
which can be used to define token threshold and window size. Redis is required to run the fourth type of algorithm.