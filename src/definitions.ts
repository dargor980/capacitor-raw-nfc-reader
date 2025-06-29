export interface RawNfcReaderPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
