workflow Checkpoint
{
   Set-AutomationVariable -Name 'Suspended' -Value $False
   $varValue = Get-AutomationVariable -Name 'Suspended'
   Write.Output $Varvalue
   Write.Output "Before Checkpoint"
   Checkpoint-workflow
   Write.Output "After checkpoint"
   $Suspended = Get-AutomationVariable -Name 'Suspended'
   Write.Output $Suspended
   if ($Suspended) 
   {
       Set-AutomationVariable -Name 'Suspended' -Value $True
       # Force an exception
       1 + "abc"
   }
   Write.Output "Runbook Complete"
}