(async () => {
    const tc = new TestCase();

    const HIGHLIGHT_TIME = 200;

    try {
        await _navigateTo('http://localhost:8080');

        await _highlight(_paragraph("Smartwach mapping service"), HIGHLIGHT_TIME);
        await _highlight(_tableHeader("Device Id"), HIGHLIGHT_TIME);
        await _highlight(_tableHeader("Customer Id"), HIGHLIGHT_TIME);
        await _highlight(_tableHeader("Temporary mapping"), HIGHLIGHT_TIME);
        await _highlight(_tableHeader("Delete mapping"), HIGHLIGHT_TIME);
        await _highlight(_cell("No Pairings Available"), HIGHLIGHT_TIME);
        tc.endOfStep('Check pairing table');

        await _highlight(_link("Export CSV"));
        tc.endOfStep('Check csv export button visibility');

        await _highlight(_paragraph("Create new Pairing"), HIGHLIGHT_TIME);
        await _highlight(_label("Device ID"), HIGHLIGHT_TIME);
        await _highlight(_textbox("deviceId"), HIGHLIGHT_TIME);
        await _highlight(_label("Customer ID"), HIGHLIGHT_TIME);
        await _highlight(_textbox("customerId"), HIGHLIGHT_TIME);
        await _highlight(_checkbox("temporary"), HIGHLIGHT_TIME);
        await _highlight(_label("temporary"), HIGHLIGHT_TIME);
        await _highlight(_button("Submit"), HIGHLIGHT_TIME);
        tc.endOfStep('Check new pairing form layout');
    } catch (e) {
        tc.handleException(e);
    } finally {
        tc.saveResult();
    }
})().then(done);