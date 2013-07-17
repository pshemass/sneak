namespace scala com.sneak.thrift

service SneakService {
  void configure(1: map<string,string> config)
}