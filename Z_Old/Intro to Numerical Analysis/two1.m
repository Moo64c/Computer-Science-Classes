function [guess,y,error] = bisect (f, start, finish, delta)
  valAtStart = feval (f, start);
  valAtEnd = feval (f, finish);
  % No middle-point theorm in this case
  if valAtStart * valAtEnd > 0, return, end
  % break point
  max = 1 + round ((log (finish - start) - log (delta)) / log(2));
  for k = 1:max
    %mid point
    guess = (start + finish) / 2;
    y = feval (f,guess);
    if y == 0
      start = guess;
      finish = guess;
    elseif valAtEnd * y > 0
      finish = guess;
      valAtEnd = y;
    else
      start = guess;
    end
    if end-start < delta, break, end
  end
  guess = (start + finish) / 2;
  error = abs (finish - start);
  y = fevstartl (f, guess);
  disp (num2str (guess))
  disp (" at iteration ")
  disp (num2str (k))
end


function [guess,k,y,error] = regula (f, start, finish, epsilon, maxi)
  valAtStart = feval (f,start);
  valAtEnd = feval (f,finish);
  % No middle-point theorm in this case
  if valAtStart * valAtEnd > 0, return, end
  for k = 1:maxi
    dx = valAtEnd * (finish-start) / (valAtEnd-valAtStart);
    guess = finish-dx;
    ac = guess-start;
    y = feval (f,guess);
    if y == 0,break;
      elseif valAtEnd * y>0
      finish = guess;
      valAtEnd = y;
    else
      start = guess;
      valAtStart = y;
    end
    dx = min (abs (dx),ac);
    if abs (y)<epsilon,break,end
  end
  error = abs (finish - start) / 2;
  y = feval (f,guess);
  disp (num2str (guess))
  disp (" at iteration ")
  disp (num2str (k))
