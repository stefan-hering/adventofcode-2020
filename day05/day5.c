# include <stdio.h>


int main(){
    FILE *fp;
    char buff[16];
    int seats[128][8];

    fp = fopen("./input", "r");

    int scan = fscanf(fp, "%s", buff);

    int highest = 1;

    while (scan != EOF) {
        int row = 0;
        for(int i = 0; i < 7; i++) {
            row = row << 1;
            row = row | (buff[i] == 'B' ? 1 : 0);
        }
        int col = 0;
        for(int i = 7; i < 10; i ++) {
            col = col << 1;
            col = col | (buff[i] == 'R' ? 1 : 0);
        }

        int seat = row * 8 + col;

        if(highest < seat) {
            highest = seat;
        }

        scan = fscanf(fp, "%s", buff);
    }

    printf("Highest seatno: %d", highest);

    fclose(fp);
    return 0;
}
