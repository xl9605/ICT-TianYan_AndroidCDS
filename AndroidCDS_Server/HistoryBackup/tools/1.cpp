#include<unistd.h>
#include<stdio.h>
#include<cstring>
#include<pthread.h>
#define MAX_THREAD 2

pthread_t thread[MAX_THREAD];
pthread_mutex_t mut;

int num = 0;
void* thread1(void *args)
{
    printf("hi, I am thread1\n");
    for(int i=0; i < 6; i++){
        pthread_mutex_lock(&mut);
        printf("[thread1]num=%d, i=%d\n", num, i);
            num ++;
        pthread_mutex_unlock(&mut);
        sleep(2);
    }
    pthread_exit(NULL);
}

void* thread2(void *args)
{
    printf("hi, I am thread2\n");
    for(int i = 0; i < 10; i++)
    {
        pthread_mutex_lock(&mut);
        printf("[thread2]num=%d, i=%d\n", num, i);
            num ++;
        pthread_mutex_unlock(&mut);
        sleep(3);
    }
    pthread_exit(NULL);
}

void thread_create()
{
    memset(&thread, 0, sizeof(thread));
    if( 0!=pthread_create(&thread[0], NULL, thread1, NULL))
        printf("thread1 create faild\n");
    else
            printf("thread1 established\n");

    if( 0 != pthread_create(&thread[1], NULL, thread2, NULL))
        printf("thread2 create faild\n");
    else
        printf("thread2 established\n");

}

void thread_wait()
{
    if(0 != thread[0])
    {
        pthread_join(thread[0], NULL);
        printf("thread 1 is over\n");
    }

    if(0 != thread[1])
    {
        pthread_join(thread[1], NULL);
        printf("thread 2 is over\n");
    }
}

int main()
{
    pthread_mutex_init(&mut, NULL);
    printf("main thread: creating threads...\n");
    thread_create();
    printf("main thread: waiting threads to accomplish task...\n");
    thread_wait();
    return 0;
}
