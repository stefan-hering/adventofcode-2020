# include <stdio.h>

int seats[1024];

int main(){
    FILE *fp;
    char buff[16];

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

        seats[seat] = seat;
        scan = fscanf(fp, "%s", buff);
    }

    for(int i = 1; i < 1023; i++) {
        if(seats[i] == 0 && seats[i+1] != 0 && seats[i - 1] != 0) {
            printf("Found seat: %d\n", i);
        }
    }

    printf("Highest seatno: %d\n", highest);

    fclose(fp);
    return 0;
}
