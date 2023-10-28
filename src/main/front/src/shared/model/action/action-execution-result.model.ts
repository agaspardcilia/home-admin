export interface ActionExecutionResult {
    outcome: string;
    returnCode: number;
    stdin: string;
    stdout: string;
    exception: string;
}
