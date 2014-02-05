namespace scala com.sneak.thrift

struct Message {
    1: required i64 timestamp;
    2: required string name;
    3: required double value;
    4: required string host;
    5: required string application
    6: required map<string,string> tags
}

service SneakService {
  void configure(1: map<string,string> config)
}