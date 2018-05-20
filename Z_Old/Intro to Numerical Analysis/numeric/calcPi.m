
function [est_pi, error, d] = calcPi(n)
       
    pi_estimate = 2^n;

    last = 0;

    while (n > 1)
        last = sqrt(vpa(2 + last));
        n = n - 1;
    end

    last = sqrt(2-last);
    est_pi = pi_estimate * last;
    error = vpa(pi) - vpa(est_pi);

    d = 1 - log10(error);

return

