
While (true)
   WinWaitActive("[CLASS:Tco17kMainFM]")
   Send("^n")
   While (WinActive("[CLASS:Tco17kMainFM]"))
   Send("^n")
   Sleep (15000)
   WEnd
WEnd



