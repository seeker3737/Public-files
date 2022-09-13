#include<stdio.h>

main(){
    int a,b;
    scanf("%d",&a);
    scanf("%d",&b);

    int x,y;
    int flag_bridge[a][b];
    for(x=0;x<a;x++){
        for(y=0;y<b;y++){
            flag_bridge[x][y]=0;
        }
    }

    int str1,str2;
    while(scanf("%d %d", &str1,&str2)!=EOF){
        flag_bridge[str1][str2]=1;
    }


    int i,j;
    for(i=1;i<b+1;i++){
	printf("%d  ",i);
}
    printf("\n");


    for(i=0;i<a;i++){
        for(j=0;j<b;j++){
            printf("|");
            if(flag_bridge[i][j]==1){
                printf("--");
	    }else{
                printf("  ");
            }
        }
        printf("\n");
    }
    for(i=65;i<b+65;i++){
	printf("%c  ",i);
}
    printf("\n");
}
