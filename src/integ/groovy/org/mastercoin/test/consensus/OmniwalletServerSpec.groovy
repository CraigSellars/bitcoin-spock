package org.mastercoin.test.consensus

import org.mastercoin.consensus.OmniwalletConsensusTool

import static org.mastercoin.CurrencyID.*
import spock.lang.Specification

/**
 * Functional test for getting consensus data from Omni API
 */
class OmniwalletServerSpec extends Specification {

    def "Can get Omniwallet block height"() {
        setup:
        OmniwalletConsensusTool omniFetcher = new OmniwalletConsensusTool(OmniwalletConsensusTool.OmniHost_Live)

        when: "we get a block height"
        /* Private method, but we can still call it with Groovy for a test */
        def blockHeight = omniFetcher.currentBlockHeight()

        then: "it looks valid"
        /* TODO:  Check to make sure it's within 15 blocks of Master Core, or something like that? */
        blockHeight >= 315121
    }

    def "Can get Omniwallet consensus data"() {
        setup:
        OmniwalletConsensusTool omniFetcher = new OmniwalletConsensusTool(OmniwalletConsensusTool.OmniHost_Live)

        when: "we get data"
        def omniSnapshot = omniFetcher.getConsensusSnapshot(MSC)

        then: "something is there"
        omniSnapshot.currencyID == MSC
        omniSnapshot.blockHeight > 323000  // Greater than a relatively recent main-net block
        omniSnapshot.entries.size() >= 1
    }


}
