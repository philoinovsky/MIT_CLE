void test(int d)
{
  long long A[10];
  long long i;

  if (d == 4) { return ; }
  for (i = 0; i < 10; i += 1)
  {
    A[i] = i % d;  
  }

  test(d + 1);
  printf("result should be: ");
  for(i = 0; i < 10; i += 1 ) {
    printf ( "%ld ", A[i] );
  }
  printf("\n");
}

void main () {
  test(1);
}