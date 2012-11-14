function [avg, dev, maxi, mini, med, tsize] = ioscm_req(input, type)
col_tid = 1;
col_st = 2;
col_et = 3; %nano
col_off = 5;
col_len = 6; %bytes

%tids = unique(input(:, col_tid));
% rawTlog = cell(length(tids),1);
% for i = 1:length(tids)
%     rawTlog{i} = input(input(:,col_tid) == tids(i), :);
% end

intv = input(:, col_et) - input(:, col_st); %Request Serving Time

avg = mean(intv);
dev = std(intv);
maxi = max(intv);
mini = min(intv);
med = median(intv);

span = max(input(:, col_et)) - min(input(:, col_st));
tsize = sum(input(:, col_len))/1000000; %MB 