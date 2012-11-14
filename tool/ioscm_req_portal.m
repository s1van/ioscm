function ioscm_req_portal(pwd, fname, type)

input = dlmread(strcat(pwd, '/', fname));
[avg, std, maxi, mini, med, tsize] = ioscm_req(input, type);

fprintf( '%.2f\t\t%.2f\t\t%.2f\t\t%.2f\t\t%.2f\t\t%.2f', avg, std, maxi, mini, med, tsize);