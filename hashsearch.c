#include<stdio.h>
#include<stdlib.h>
#include <string.h>
#define HASHSIZE 101
struct city{
    char *name; /* 都市名 */
    float lat; /* 緯度 */
    float lng; /* 経度 */
    char *country; /* 国名 */
    int pop; /* 人口(population) */
    struct city *next;
};

struct city *hashtable[HASHSIZE];
struct city *hashtable2[HASHSIZE];
int hash(char *key){
  unsigned int hashval = 0;
  while(*key!='\0'){
    hashval += *key;
    key++;
  }
  return hashval % HASHSIZE;//アドレス割る101,余りは絶対0~100のどれか
}

void add(char *name, float lat, float lng, char *country, int pop){
    struct city *tmp;
    int i =hash(name);//ハッシュ値取得
    tmp = malloc(sizeof(struct city));
    tmp->name = (char *)malloc(sizeof(char)*1024);
    tmp->country=(char *)malloc(sizeof(char)*1024);
    tmp->next=(struct city *)malloc(sizeof(struct city));
    strcpy((*tmp).name, name);
    tmp->lat=lat;
    tmp->lng=lng;
    strcpy((*tmp).country, country);
    tmp->pop=pop;
    (*tmp).next=hashtable[i];
    hashtable[i]=tmp;
}
void add2(char *name, float lat, float lng, char *country, int pop){
    struct city *tmp;
    int i =hash(country);//国名ハッシュ値取得
    tmp = malloc(sizeof(struct city));
    tmp->name = (char *)malloc(sizeof(char)*1024);
    tmp->country=(char *)malloc(sizeof(char)*1024);
    tmp->next=(struct city *)malloc(sizeof(struct city));
    strcpy((*tmp).name, name);
    tmp->lat=lat;
    tmp->lng=lng;
    strcpy((*tmp).country, country);
    tmp->pop=pop;
    (*tmp).next=hashtable2[i];
    hashtable2[i]=tmp;
}

void dist(){
    int i;
    int j=0;
    struct city *ptr;
    
    printf("--HASHSIZE: %d--\n",HASHSIZE);
    
    for(i=0;i<HASHSIZE;i++){
	ptr=hashtable[i];
	if(ptr!=NULL){
	    while((*ptr).next!=NULL){
		j++;
		ptr=ptr->next;
	    }
	}	
	printf("TABLE[%d]: %d\n",i,j);
	j=0;//reset
    }    
}


void print_city(char *name){
    struct city *ptr;
    int i=hash(name);//名前からハッシュ値確認
    ptr=hashtable[i];

    while(ptr!=NULL){
	if(strcmp(ptr->name, name)==0){//注意：strcmpは一致したら０が返ってくる
	    printf("City Name:%s lat: %f lng: %f [%s] Population: %d\n",ptr->name,ptr->lat,ptr->lng,ptr->country,ptr->pop);
	    return;
	}
	ptr=ptr->next;
    }
    printf("None. \n");
}


void print_cities(char *country){
    struct city *ptr;//国名でハッシュ値とったハッシュテーブルポインタ
    //struct city *tmp;//指定した国名だけの構造体用1
    //struct city *a;//指定した国名だけの構造体用2
    int i=hash(country);//国名ハッシュ値取得
    ptr=hashtable2[i];
    //tmp=malloc(sizeof(struct city));
    //a=malloc(sizeof(struct city));
    //a=NULL;

    //ここでソートしたい
    //ソートが終わったら

    while(ptr!=NULL){//日本の都市だけ表示
	if(strcmp(ptr->country,country)==0){
	    printf("%s [%s] %d\n",ptr->name,country,ptr->pop);
	    ptr=ptr->next;
	}
	ptr=ptr->next;
    }
}



int main(){
    char name[1024];
    char country[1024];
    float lat, lng;
    int pop, i, f;
    char *filename = "worldcities.txt";
    FILE *fp;
    char buffer[1024];
/*  任意課題用
  char buffer2[1024];
  int n, m;  
*/
    
    for(i=0; i<HASHSIZE; i++){
	hashtable[i]=NULL;
	hashtable2[i]=NULL;
    }
    fp = fopen("city.txt","r");
    while((f = fscanf(fp,"%s %f %f %s %d",name,&lat,&lng,country,&pop))!=EOF){
	add(name, lat, lng, country, pop);
	add2(name, lat, lng, country, pop);
    }
    fclose(fp);
    dist();

    printf("City name? ");
    scanf("%s", buffer);
    print_city(buffer); 

    printf("Country name? ");
    scanf("%s", buffer);
    print_cities(buffer);

/* 任意課題テスト用
  printf("Optional input? ");
  scanf("%s %d %d %s", buffer, &n, &m, buffer2);
  print_cities2(buffer, n, m, buffer2);
*/
}
