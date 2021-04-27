long long a[10];
long long b[10];

void main() {
  int i;
  for (i = 9; i >= 0; i -= 1) {
    a[i] = i + 2;
  }
  for (i = 8; i >= 0; i -= 3) {
    b[i] = a[i] + i;
  }
  printf("The value of i should be -1, actual: %lld\n", i);
  printf("The value of a[] should be: 2 3 4 5 6 7 8 9 10 11\n");
  printf("                    actual: %lld %lld %lld %lld %lld %lld %lld %lld %lld %lld\n", a[0], a[1], a[2], a[3], a[4], a[5], a[6], a[7], a[8], a[9]);
  printf("The value of b[] should be: 0 0 6 0 0 12 0 0 18 0\n");
  printf("                    actual: %lld %lld %lld %lld %lld %lld %lld %lld %lld %lld\n", b[0], b[1], b[2], b[3], b[4], b[5], b[6], b[7], b[8], b[9]);
}