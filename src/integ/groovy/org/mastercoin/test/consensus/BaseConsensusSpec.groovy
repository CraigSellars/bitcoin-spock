package org.mastercoin.test.consensus

import org.mastercoin.BaseMainNetSpec
import org.mastercoin.CurrencyID
import org.mastercoin.consensus.ConsensusComparison
import org.mastercoin.consensus.ConsensusFetcher
import org.mastercoin.consensus.MasterCoreConsensusTool
import org.mastercoin.consensus.OmniwalletConsensusTool
import spock.lang.Shared
import spock.lang.Subject
import spock.lang.Unroll

/**
 * Base class for Consensus Comparisons
 * Makes sure the block height of both snapshots matches and
 * also compares the balance of every address in the <b>union</b> of the two snapshots.
 */
abstract class  BaseConsensusSpec extends BaseMainNetSpec {
    @Shared @Subject
    ConsensusComparison comparison

    /**
     * Set up a ConsensusComparison for a particular currency
     * @param referenceFetcher ConsensusFetcher for a (remote) reference server
     * @param id CurrencyID to compare
     */
    void setupComparisonForCurrency(ConsensusFetcher referenceFetcher, CurrencyID id) {
        def mscFetcher = new MasterCoreConsensusTool(client)
        def mscSnapshot = mscFetcher.getConsensusSnapshot(id)

        def referenceSnapshot = referenceFetcher.getConsensusSnapshot(id)

        comparison = new ConsensusComparison(mscSnapshot, referenceSnapshot)
        println "Setup comparison height1 = ${comparison.c1.blockHeight}, height2 = ${comparison.c2.blockHeight}"
    }

    @Unroll
    def "blockHeight #leftHeight == #rightHeight (#currency)"() {
        expect:
        leftHeight == rightHeight

        where:
        currency = comparison.c1.currencyID
        leftHeight = comparison.c1.blockHeight
        rightHeight = comparison.c2.blockHeight
    }

    @Unroll
    def "#address #entry1 == #entry2"() {
        expect:
        entry1 == entry2

        where:
        [address, entry1, entry2] << comparison
    }

}
