#include <stdio.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <errno.h>

#define dest_ip "127.0.0.1"
#define dest_port 60001

int main() {
  // Variables for socket
  int my_socket, r;
  struct sockaddr_in my_addr;
  unsigned int len = 5000;
  unsigned char data[len];
  
  data[0] = 0x00;
  data[1] = 0x01;

  // IPv4, Port #, IP Addr
  my_addr.sin_family = AF_INET;
  my_addr.sin_port = htons(dest_port);
  r = inet_aton(dest_ip,&(my_addr.sin_addr));
  if(r == 0) {
    printf("Error converting IP address!\n");
  }

  // Create socket
  my_socket = socket(AF_INET, SOCK_DGRAM, 0);
  
  // Write data
  r = sendto(my_socket, data, 2, 0, (struct sockaddr*)&my_addr,
              sizeof(struct sockaddr_in));
  if(r == -1) {
    printf("Errno is: %i\n",errno);
  }
  close(my_socket);
}
